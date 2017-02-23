import os
import urllib.request


def download_ngram_file(
    url_suffix, save_location=None, gram_size=2,
    url_prefix="http://storage.googleapis.com/books/ngrams/books/googlebooks-eng-all-GRAM_SIZE-20120701-"):  # noqa: E501
    """
    Defaults:
    Downloads the 2gram file.
    Downloads files from the English corpus version 20120701.

    WARNING: Does not validate HTTPS certificates if download is via HTTPS.
    """

    url = (
        url_prefix.replace("GRAM_SIZE", str(gram_size) + "gram")
        + url_suffix + ".gz")

    if save_location is None:
        os.makedirs("data", exist_ok=True)
        save_location = "data/" + str(gram_size) + "gram_" + url_suffix

    if os.path.exists(save_location):
        print("Already Downloaded: " + save_location)
    else:
        urllib.request.urlretrieve(url, save_location + "_downloading")
        os.rename(save_location + "_downloading", save_location)

    return save_location
