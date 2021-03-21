import io

from minio import Minio
import uuid


def handle(req):
    """handle a request to the function
    Args:
        req (str): request body
    """

    client = Minio(
        "play.min.io",
        access_key="minio",
        secret_key="minio123"
    )

    output = "s3: " + req
    output = output.encode()
    print(output)

    result = client.put_object(
        "mybucket", str(uuid.uuid4()), io.BytesIO(output), len(output),
    )
    print(
        "created {0} object; etag: {1}, version-id: {2}".format(
            result.object_name, result.etag, result.version_id,
        ),
    )

    return output
