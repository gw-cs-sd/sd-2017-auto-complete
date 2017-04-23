import gzip
import json
import os

default_weights = [1, 1, 1, 1]


def write_completions(f, front, completions, topn, weights, file_started):
    if len(completions) == 0:
        return

    by_n = [[], [], [], []]

    for completion in completions:
        n = completion["completion"].count(" ") + 1
        by_n[n - 1].append(completion)

    for i in range(0, 4):
        if len(by_n[i]) == 0:
            continue

        by_n[i].sort(key=lambda x: x["score"], reverse=True)

        if len(by_n[i]) > topn:
            by_n[i] = by_n[i][:topn]

        json_to_write = {
            "completions": by_n[i],
            "text": front["text"],
            "gramLength": front["text"].count(" ") + 1
        }

        if file_started[i]:
            f[i].write(", ")
        else:
            file_started[i] = True

        f[i].write(json.dumps(json_to_write))


def export(
        temp_file_name, export_file_name, weights=default_weights, topn=10):
    if len(weights) is not 4:
        raise ValueError("weights parameter must be 4 elements long.")

    export_fns = []
    dot_pos = export_file_name.find(".")
    if dot_pos == -1:
        for i in range(1, 5):
            export_fns.append(export_file_name + str(i))
    else:
        for i in range(1, 5):
            export_fns.append(
                export_file_name[:dot_pos] + str(i)
                + export_file_name[dot_pos:])

    file_started = [False, False, False, False]

    with gzip.open(
            temp_file_name, "rt", newline='', encoding="utf8"
            ) as temp_f, gzip.open(
            export_fns[0] + "_exporting", "wt", encoding="utf8"
            ) as export_f1, gzip.open(
            export_fns[1] + "_exporting", "wt", encoding="utf8"
            ) as export_f2, gzip.open(
            export_fns[2] + "_exporting", "wt", encoding="utf8"
            ) as export_f3, gzip.open(
            export_fns[3] + "_exporting", "wt", encoding="utf8"
            ) as export_f4:

        # fronts - current leading words/tokens
        # fronts[0] = first word, [1] = first two words, ... ,
        # [3] = first four words
        # Each element is a dict: {"text": "", "count": ""}
        fronts = [None, None, None, None]
        export_fs = [export_f1, export_f2, export_f3, export_f4]
        completions = [[], [], [], []]

        for i in export_fs:
            i.write("[")

        for line in temp_f:
            l = line.split("\t")
            # l[0] - ngram
            # l[1] - count
            # l[2] - volume count

            words = l[0].split(" ")

            if len(words) > 5:
                raise ValueError("ngram has more than five tokens.")

            for i in [4, 3, 2, 1]:
                if len(words) <= i:
                    if fronts[i - 1] is not None:
                        write_completions(
                            export_fs, fronts[i - 1], completions[i - 1],
                            topn, weights, file_started)

                    completions[i - 1] = []

                    if len(words) == i:
                        fronts[i - 1] = {
                            "text": " ".join(words),
                            "count": int(l[1])
                        }
                    else:
                        fronts[i - 1] = None

                    continue

                l_back = " ".join(words[i:])

                percent = int(l[1]) / fronts[i - 1]["count"]

                completions[i - 1].append({
                    "count": int(l[1]),
                    "completion": l_back,
                    "percent": percent,
                    "score": (percent * weights[i - 1])})

        for i in [4, 3, 2, 1]:
            if fronts[i - 1] is not None:
                write_completions(
                    export_fs, fronts[i - 1], completions[i - 1],
                    topn, weights, file_started)

        for i in export_fs:
            i.write("]")

    for fn in export_fns:
        os.rename(fn + "_exporting", fn)
