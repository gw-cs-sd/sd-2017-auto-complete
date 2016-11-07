import csv
import gzip
import json
import urllib.request


def download_ngram_file(
    url_suffix, save_location=None, n_value=2,
    url_prefix="http://storage.googleapis.com/books/ngrams/books/googlebooks-eng-all-2gram-20120701-"):  # noqa: E501
    """
    Currently only works with bigram files.

    Does not validate HTTPS certificates if download is via HTTPS.
    """

    # TODO: Add code to replace the n in "2gram" in the url prefix to the
    # specified n.
    url = url_prefix + url_suffix + ".gz"

    if save_location is None:
        save_location = "data/" + str(n_value) + "gram_" + url_suffix

    urllib.request.urlretrieve(url, save_location)
    # TODO Error handling

    return save_location

#
# def bigram_text_format(word1, word2, appearances):
#    return word1 + " " + word2 + " " + appearances + "\n"


# def bigram_json_format(word1, word2, appearances):
    # {"gram": word, "wordFrequency": number, "bookFrequency": number}
#    return (json.dumps({
#        "word1": word1, "word2": word2, "appearances": appearances}) +
#        ",\n")


def export(ngram_file, save_location):
    """
    Exports json file.
    """

    with gzip.open(ngram_file, "rt", newline='') as to_add:
        with gzip.open(save_location, "at") as f:
            dialect = csv.Sniffer().sniff(to_add.read(4096))
            to_add.seek(0)
            reader = csv.reader(to_add, dialect)

            word1 = None
            word2 = None
            appearances = 0

            following_frequency = []

            f.write("[")
            for row in reader:
                words = row[0].split()

                # Remove part of speech labels.
                if words[0].find("_") != -1:
                    words[0] = words[0][:(words[0].find("_"))]

                # If same set of words
                if word1 == words[0] and word2 == words[1]:
                    appearances += int(row[2])
                # If same first word only
                elif word1 == words[0]:
                    following_frequency.append(
                        {"appearances": appearances, "text": word2})
                    word2 = words[1]
                    appearances = int(row[2])
                else:
                    if (word1 is not None) and (word2 is not None):
                        following_frequency.sort(
                            key=lambda x: x["appearances"], reverse=True)

                        if len(following_frequency) > 10:
                            following_frequency = following_frequency[:10]

                        json_to_write = {
                            "following_frequency": following_frequency,
                            "word": word1
                        }

                        f.write(json.dumps(json_to_write))
                        f.write(",")
                    word1 = words[0]
                    word2 = words[1]
                    appearances = int(row[2])
                    following_frequency = []

            following_frequency.sort(
                key=lambda x: x["appearances"], reverse=True)

            if len(following_frequency) > 10:
                following_frequency = following_frequency[:10]

            json_to_write = {
                "following_frequency": following_frequency,
                "word": word1
            }
            f.write(json.dumps(json_to_write))

            f.write("]")
