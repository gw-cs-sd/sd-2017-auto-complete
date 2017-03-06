from contextlib import ExitStack
import gzip
import json
import os

default_weights = [1, 1, 1, 1]


def export(
        temp_file_name, export_file_name, weights=default_weights, topn=40):
    if len(default_weights) is not 4:
        raise ValueError("weights parameter must be 4 elements long.")

    with gzip.open(
            temp_file_name, "rt", newline='', encoding="utf8"
            ) as temp_f, gzip.open(
            export_file_name + "_exporting", "wt", encoding="utf8"
            ) as export_f:

        front = None

        fronts = {}

        completions = []

        export_f.write("[")

        for line in temp_f:
            l = line.split("\t")
            # l[0] - ngram
            # l[1] - count
            # l[2] - volume count

            words = l[0].split()

            l_front = words[0]
            l_back = " ".join(words[1:])

            if front == l_front:
                completions.append({
                    "count": int(l[1]),
                    "completion": l_back})
            else:
                if front is not None:
                    for completion in completions:
                        n = completion["completion"].count(" ") + 1
                        completion["count"] *= weights[n - 1]

                    completions.sort(
                        key=lambda x: x["count"], reverse=True)

                    if len(completions) > topn:
                        completions = completions[:topn]

                    json_to_write = {
                        "completions": completions,
                        "text": front
                    }

                    export_f.write(json.dumps(json_to_write))
                    export_f.write(",")

                front = l_front
                completions = []

                completions.append({
                    "count": int(l[1]),
                    "completion": l_back})

        # Last front.
        for completion in completions:
            n = completion["completion"].count(" ") + 1
            completion["count"] *= weights[n - 1]

        completions.sort(
            key=lambda x: x["count"], reverse=True)

        if len(completions) > topn:
            completions = completions[:topn]

        json_to_write = {
            "completions": completions,
            "text": front
        }

        export_f.write(json.dumps(json_to_write))
        export_f.write("]")

    os.rename(export_file_name + "_exporting", export_file_name)
