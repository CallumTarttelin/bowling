from os import listdir


def get_adoc(dr):
    adoc = {}
    for directory in listdir(dr):
        adoc[directory] = []
        for file in listdir(dr + "/" + directory):
            if file.endswith(".png"):
                adoc[directory].append(adocify(file, directory + "/"))
    return adoc


def adocify(name, directory):
    return "\n.{1}\nimage:{0}[{1},pdfwidth=100%]\n".format(directory + name, name[:-4])


def prettify(inp):
    string = ""
    for key in inp:
        string += "== {0}\n=== Test Results \n\n\n=== Pictures \n".format(key)
        for adoc in inp.get(key):
            string += adoc
        string += "\n\n\n"
    return string


def ezui():
    while True:
        directory = input("dir:\n")
        print(get_adoc(directory))

