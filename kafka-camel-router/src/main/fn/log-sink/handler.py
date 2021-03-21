import logging

logging.basicConfig(level=logging.DEBUG)


def handle(req):
    """handle a request to the function
    Args:
        req (str): request body
    """

    logging.info(req)

    return "FN Python trigger: " + req
