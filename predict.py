import gzip
import json
import time


def predict(word1, index_file):
    """
    WARNING: LOADS ENTIRE INDEX JSON INTO MEMORY!
    """

    start = time.perf_counter()

    with gzip.open(index_file, "rt") as index:
        idx = json.loads(index.read())

        result = next((x for x in idx if x["word"] == word1), None)

        if ((result is None) or (len(result["following_frequency"]) == 0)):
            print("\nNo prediction available.")
        else:
            print("\nPredicted next word for", word1)
            print("===================================")

            for w in result["following_frequency"]:
                print(w["text"], "with", w["appearances"], "occurences")

    end = time.perf_counter()

    t = end - start
    print("\nTime:", str(t), "seconds\n")
