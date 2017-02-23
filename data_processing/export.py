import gzip
import json
import os


def export(temp_file_name, export_file_name):
    with gzip.open(
            temp_file_name, "rt", newline='', encoding="utf8") as temp_f:
        with gzip.open(
                export_file_name + "_exporting", "wt", encoding="utf8"
                ) as export_f:

            front = None
            back = None

            appearances = 0
            following_frequency = []

            export_f.write("[")

            for line in temp_f:
                l = line.split()

                if len(l) >= 4:
                    l_front = l[0]

                    # All but the first and the last two.
                    l_back = " ".join(l[1:-2])

                    # If same ngram
                    if front == l_front and back == l_back:
                        appearances += int(l[-2])
                    # If same part 1
                    elif front == l_front:
                        following_frequency.append(
                            {"appearances": appearances, "text": back})

                        back = l_back
                        appearances = int(l[-2])
                    else:
                        if front is not None and back is not None:
                            following_frequency.append(
                                {"appearances": appearances, "text": back})

                            following_frequency.sort(
                                key=lambda x: x["appearances"], reverse=True)

                            if len(following_frequency) > 10:
                                following_frequency = following_frequency[:10]

                            json_to_write = {
                                "following_frequency": following_frequency,
                                "word": front
                            }

                            export_f.write(json.dumps(json_to_write))
                            export_f.write(",")

                        front = l_front
                        back = l_back
                        appearances = int(l[-2])
                        following_frequency = []

            # Write json for last front
            following_frequency.append(
                {"appearances": appearances, "text": back})

            following_frequency.sort(
                key=lambda x: x["appearances"], reverse=True)

            if len(following_frequency) > 10:
                following_frequency = following_frequency[:10]

            json_to_write = {
                "following_frequency": following_frequency,
                "word": front
            }

            export_f.write(json.dumps(json_to_write))
            export_f.write("]")

    os.rename(export_file_name + "_exporting", export_file_name)
