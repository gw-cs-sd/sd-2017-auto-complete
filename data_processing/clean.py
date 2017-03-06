import re


def clean(line):
    """
    line[0] - ngram
    line[1] - appearances
    line[2] - volume appearances
    """
    words = line[0].split(" ")

    for i in range(0, len(words)):
        # Remove part of speech labels. TODO improve detection.
        if words[i].find("_") != -1:
            words[i] = words[i][:(words[i].find("_"))]

        # Make lowercase
        words[i] = words[i].lower()

        if re.search('[a-z]', words[i]) is None:
            return False

    return "\t".join([(" ".join(words))] + line[1:])
