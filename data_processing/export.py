import gzip
import json
import os

default_weights = [1, 1, 1, 1]


def write_completions(f, front, completions, topn, weights):
    if not completions:
        return

    for completion in completions:
        n = completion["completion"].count(" ") + 1
        completion["count"] *= weights[n - 1]

    completions.sort(
        key=lambda x: x["count"], reverse=True)

    if len(completions) > topn:
        completions = completions[:topn]

    json_to_write = {
        "completions": completions,
        "text": front["text"]
    }

    f.write(json.dumps(json_to_write))
    f.write(",")


def export(
        temp_file_name, export_file_name, weights=default_weights, topn=40):
    if len(default_weights) is not 4:
        raise ValueError("weights parameter must be 4 elements long.")

    with gzip.open(
            temp_file_name, "rt", newline='', encoding="utf8"
            ) as temp_f, gzip.open(
            export_file_name + "_exporting", "wt", encoding="utf8"
            ) as export_f:

        # fronts - current leading words/tokens
        # fronts[0] = first word, [1] = first two words, ... ,
        # [3] = first four words
        # Each element is a dict: {"text": "", "count": ""}
        fronts = [None, None, None, None]

        completions = [[], [], [], []]

        export_f.write("[")

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
                            export_f, fronts[i - 1], completions[i - 1],
                            topn, weights)

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

                completions[i - 1].append({
                    "count": int(l[1]),
                    "completion": l_back})

        # Last fronts.
        for i in [4, 3, 2]:
            if fronts[i - 1] is not None:
                write_completions(
                    export_f, fronts[i - 1], completions[i - 1], topn,
                    weights)

        for completion in completions[0]:
            n = completion["completion"].count(" ") + 1
            completion["count"] *= weights[n - 1]

        completions[0].sort(
            key=lambda x: x["count"], reverse=True)

        if len(completions[0]) > topn:
            completions[0] = completions[0][:topn]

        json_to_write = {
            "completions": completions[0],
            "text": fronts[0]["text"]
        }

        export_f.write(json.dumps(json_to_write))
        export_f.write("]")

    os.rename(export_file_name + "_exporting", export_file_name)
