from contextlib import ExitStack
import csv
import gzip
import heapq
import json
import os
import re
import sys
import urllib.request


def download_ngram_file(
    url_suffix, save_location=None, gram_size=2,
    url_prefix="http://storage.googleapis.com/books/ngrams/books/googlebooks-eng-all-GRAM_SIZE-20120701-"):  # noqa: E501
    """
    Defaults:
    Downloads the 2gram file.
    Downloads files from the English corpus version 20120701.

    Does not validate HTTPS certificates if download is via HTTPS.
    """

    url = (
        url_prefix.replace("GRAM_SIZE", str(gram_size) + "gram")
        + url_suffix + ".gz")

    if save_location is None:
        save_location = "data/" + str(gram_size) + "gram_" + url_suffix

    if os.path.exists(save_location):
        print("There is already a file or directory at " + save_location)
    else:
        urllib.request.urlretrieve(url, save_location)

    return save_location


def clean(line):
    """
    For version 2 (2012) of the ngram corpus:
    line[-3] - year
    line[-2] - appearances
    line[-1] - book appearances
    """
    words = line[0].split(" ")

    for i in range(0, len(words)):
        # Remove part of speech labels.
        if words[i].find("_") != -1:
            words[i] = words[i][:(words[i].find("_"))]

        # Make lowercase
        words[i] = words[i].lower()

        if re.search('[a-z]', words[i]) is None:
            return False

    return " ".join(words + line[-2:])


def clean_and_sort(list_of_files, output_file_name):
    chunk_size = 1000000
    temp_files = []

    for f in list_of_files:

        with gzip.open(f, "rt", newline='', encoding="utf8") as input_f:
            dialect = csv.Sniffer().sniff(input_f.read(4096))
            input_f.seek(0)
            reader = csv.reader(input_f, dialect, quoting=csv.QUOTE_NONE)

            lines = []
            line_count = 0

            for line in reader:
                c_line = clean(line)

                if c_line:
                    lines.append(c_line)
                    line_count += 1

                    if line_count % chunk_size == 0:
                        temp_file_name = f + "_temp" + str(
                            line_count // chunk_size)
                        with gzip.open(
                                temp_file_name,
                                "wt",
                                encoding="utf8") as temp_f:
                            for l in sorted(lines):
                                temp_f.write(l)
                                temp_f.write("\n")
                        temp_files.append(temp_file_name)
                        lines = []

            if len(lines) > 0:
                temp_file_name = f + "_temp" + str((
                    line_count // chunk_size) + 1)
                with gzip.open(
                        temp_file_name, "wt", encoding="utf8") as temp_f:
                    for l in sorted(lines):
                        temp_f.write(l)
                        temp_f.write("\n")
                temp_files.append(temp_file_name)

    with ExitStack() as stack, gzip.open(
            output_file_name, "wt", encoding="utf8") as output_file:
        file_iters = [
            stack.enter_context(
                gzip.open(f, "rt", encoding="utf8")) for f in temp_files]
        output_file.writelines(heapq.merge(*file_iters))

    for temp_f in temp_files:
        os.remove(temp_f)


def export(temp_file_name, export_file_name):
    with gzip.open(
            temp_file_name, "rt", newline='', encoding="utf8") as temp_f:
        with gzip.open(export_file_name, "wt", encoding="utf8") as export_f:

            part1 = None
            part2 = None

            appearances = 0
            following_frequency = []

            export_f.write("[")

            for line in temp_f:
                l = line.split()

                if len(l) >= 4:
                    l_part1 = l[0]

                    # All but the first and the last two.
                    l_part2 = " ".join(l[1:-2])

                    # If same ngram
                    if part1 == l_part1 and part2 == l_part2:
                        appearances += int(l[-2])
                    # If same part 1
                    elif part1 == l_part1:
                        following_frequency.append(
                            {"appearances": appearances, "text": part2})

                        part2 = l_part2
                        appearances = int(l[-2])
                    else:
                        if part1 is not None and part2 is not None:
                            following_frequency.append(
                                {"appearances": appearances, "text": part2})

                            following_frequency.sort(
                                key=lambda x: x["appearances"], reverse=True)

                            if len(following_frequency) > 10:
                                following_frequency = following_frequency[:10]

                            json_to_write = {
                                "following_frequency": following_frequency,
                                "word": part1
                            }

                            export_f.write(json.dumps(json_to_write))
                            export_f.write(",")

                        part1 = l_part1
                        part2 = l_part2
                        appearances = int(l[-2])
                        following_frequency = []

            # Write json for last part1
            following_frequency.append(
                {"appearances": appearances, "text": part2})

            following_frequency.sort(
                key=lambda x: x["appearances"], reverse=True)

            if len(following_frequency) > 10:
                following_frequency = following_frequency[:10]

            json_to_write = {
                "following_frequency": following_frequency,
                "word": part1
            }

            export_f.write(json.dumps(json_to_write))
            export_f.write("]")


def process(selection, path):
    if len(selection) is not 2:
        print("Selection must be two characters.")
        sys.exit()

    if path[-1] is not "/":
        path += "/"

    ngram_files = []

    for i in range(2, 6):
        data_file = path + "ngrams/" + selection + str(i) + ".gz"
        ngram_files.append(data_file)

        download_ngram_file(selection, save_location=data_file, gram_size=i)

    clean_and_sort(ngram_files, path + "temp_sorted.gz")
    export(path + "temp_sorted.gz", path + selection + ".json.gz")

    os.remove(path + "temp_sorted.gz")


if __name__ == '__main__':
    process(sys.argv[1], sys.argv[2])
