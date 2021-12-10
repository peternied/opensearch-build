import logging
import urllib.error
import urllib.request
from typing import List


class Distribution:
    def __init__(self, base_url: str, platform: str, architecture: str, product_name: str) -> None:
        self.base_url = base_url
        self.platform = platform
        self.architecture = architecture
        self.product_name = product_name

    class DistributionNotFound(Exception):
        def __init__(self, urls: List[str]):
            self.urls = urls
            super().__init__(f"Unable to find a distribution under urls {self.urls}")


    def find_build_root(self) -> str:
        for distribution_url in self.__possible_urls():
            manifest_url = f"{distribution_url}/manifest.yml"
            try:
                with urllib.request.urlopen(manifest_url):
                    # OK we could access the manifest, return the url
                    return distribution_url
            # except urllib.URLError as e:
            #     logging.info(f"No build manifest found at {manifest_url}")
            except urllib.error.HTTPError as e:
                e.code
            except urllib.error.URLError as e:
                e.errno
            except Exception as e:
                logging.warn(f"No build manifest found at {manifest_url}, unexpected error {e}")


        raise Distribution.DistributionNotFound(self.__possible_urls())

    def __possible_urls(self) -> List[str]:
        return [
            f"{self.base_url}/{self.platform}/{self.architecture}/builds/{self.product_name}",
            f"{self.base_url}/{self.platform}/{self.architecture}/builds"
        ]
