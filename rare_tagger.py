#! /usr/bin/python

__author__="Qingxiang Jia <qj2125@columbia.edu>"
__date__ ="$Oct 10, 2014"

import sys
import json


class Counts:
    def __init__(self):
        self.unary = {}
        self.taggedtrees = []

    def count(self, tree):
        if isinstance(tree, basestring):
            return

        if len(tree) == 3:
            # It is a binary rule.
            # Recursively count the children.
            self.count(tree[1])
            self.count(tree[2])
        elif len(tree) == 2:
            # It is a unary rule.
            y1 = tree[1]
            self.unary.setdefault(y1, 0)
            self.unary[y1] += 1

    def tagtree(self, tree):
        self.taggedtrees.append(tree)
        self.tag(tree)

    def tag(self, tree):
        if isinstance(tree, basestring):
            return

        if len(tree) == 3:
            self.tag(tree[1])
            self.tag(tree[2])
        elif len(tree) == 2:
            # It is a unary rule.
            y1 = tree[1]
            if self.unary[y1] < 5:
                tree[1] = "_RARE_"

    def println(self):
        for tree in self.taggedtrees:
            print json.dumps(tree)

def main(parse_file):
    counter = Counts()
    for l in open(parse_file):
        t = json.loads(l)
        counter.count(t)
    for l in open(parse_file):
        t = json.loads(l)
        counter.tagtree(t)
    counter.println()


def usage():
        sys.stderr.write("""
        Usage: python count_cfg_freq.py [tree_file] Print the counts of a corpus of trees.\n""")


if __name__ == "__main__":
    if len(sys.argv) != 2:
        usage()
        sys.exit(1)
    main(sys.argv[1])