# python format_check.py file_path

import gzip
import sys

with gzip.open(sys.argv[1], "rt", newline='', encoding="utf8") as f:
    first_line = f.readline()
    f.seek(0)

    tabs = first_line.count("\t")
    print("Expected tabs: {0}".format(tabs))
    spaces = first_line.count(" ")
    print("Expected spaces: {0}\n".format(spaces))
    line_count = 0

    for line in f:
        line_count += 1

        tab_count = line.count("\t")
        if tab_count is not tabs:
            print("Line {0} has {1} tabs.".format(line_count, tab_count))
        space_count = line.count(" ")
        if space_count is not spaces:
            print("Line {0} has {1} spaces.".format(line_count, space_count))

    print("Lines: {0}".format(line_count))
