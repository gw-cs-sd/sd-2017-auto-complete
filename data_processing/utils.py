import time


def print_duration(func, message=None):
    t = time.perf_counter()
    func()
    if time.perf_counter() - t > 0.001:
        print(message + " " + str(time.perf_counter() - t) + " seconds.")
