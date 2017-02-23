from contextlib import ExitStack
import gzip
import heapq
import os
import time

from clean import clean


def sort(list_of_files, output_fn):
    chunk_size = 1000000
    temp_files = []

    t = time.perf_counter()
    for f in list_of_files:

        with open(f, "rt", newline='', encoding="utf8") as input_f:
            lines = []
            line_count = 0

            for line in input_f:
                l = line.split("\t")
                c_l = clean(l)

                if c_l:
                    lines.append(c_l)
                    line_count += 1

                    if line_count % chunk_size == 0:
                        temp_fn = f + "_temp" + str(line_count // chunk_size)
                        with open(temp_fn, "wt", encoding="utf8") as temp_f:
                            for s_l in sorted(lines):
                                temp_f.write(s_l)
                        temp_files.append(temp_fn)
                        lines = []

            if len(lines) > 0:
                temp_fn = f + "_temp" + str((line_count // chunk_size) + 1)
                with open(temp_fn, "wt", encoding="utf8") as temp_f:
                    for s_l in sorted(lines):
                        temp_f.write(s_l)
                temp_files.append(temp_fn)
    print(
        "     Cleaning and sorting within chunks: " +
        str(time.perf_counter() - t) + " seconds.")

    t = time.perf_counter()
    with ExitStack() as stack, gzip.open(
            output_fn + "_sorting", "wt", encoding="utf8") as output_file:
        file_iters = [
            stack.enter_context(
                open(f, "rt", encoding="utf8")) for f in temp_files]
        output_file.writelines(heapq.merge(*file_iters))
    os.rename(output_fn + "_sorting", output_fn)
    print(
        "     Temp file merging and sorting: " +
        str(time.perf_counter() - t) + " seconds.")

    t = time.perf_counter()
    for temp_f in temp_files:
        os.remove(temp_f)
    print(
        "     Temp file deletion: " +
        str(time.perf_counter() - t) + " seconds.")
