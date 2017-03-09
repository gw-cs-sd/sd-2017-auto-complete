import sys

from condense import condense
from download import download_ngram_file
from export import export
from sort import sort
from utils import print_duration


def process(args):
    # mode_d means download only, mode_dc means download and condense only
    if args[1] == "mode_d" or args[1] == "mode_dc":
        selection = args[2]
        path = args[3]
    else:
        selection = args[1]
        path = args[2]

    jobs = []

    if len(selection) is 2:
        jobs.append(selection)
    elif len(selection) is 1:
        letters = "bcdfghjklmnpqrstvwxyzaeiou"
        for i in range(0, 26):
            jobs.append(selection + letters[i])
    else:
        print("Selection must be one or two characters.")
        sys.exit()

    if path[-1] is not "/":
        path += "/"

    for job in jobs:
        ngram_files = []

        for i in range(1, 6):
            jb = job[:1] if i is 1 else job

            data_file = path + "raw/" + jb + str(i) + ".gz"
            condensed_file = path + "condensed/" + jb + str(i) + "_cV2"

            print_duration(
                lambda: download_ngram_file(
                    jb, save_location=data_file, gram_size=i),
                jb + str(i) + " download:")

            if args[1] == "mode_d":
                continue

            print_duration(
                lambda: condense(data_file, condensed_file),
                jb + str(i) + " condense:")

            ngram_files.append(condensed_file)

        if args[1] == "mode_d" or args[1] == "mode_dc":
            continue

        sorted_fn = path + "sorted/" + job + "_sV2.gz"
        print_duration(
            lambda: sort(ngram_files, sorted_fn),
            "Data cleaning and sorting:")

        exported_fn = path + job + ".json.gz"
        print_duration(
            lambda: export(sorted_fn, exported_fn),
            "Data export:")


if __name__ == '__main__':
    process(sys.argv)
