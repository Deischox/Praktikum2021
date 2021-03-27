import glob
import unicodedata as ud

files = glob.glob("C:/Users/Silas/Downloads/test-wordcount13-20210305T154527Z-001/test-wordcount14/*")

latin_letters= {}
def is_latin(uchr):
    try: return latin_letters[uchr]
    except KeyError:
         return latin_letters.setdefault(uchr, 'LATIN' in ud.name(uchr))

def only_roman_chars(unistr):
    return all(is_latin(uchr)
           for uchr in unistr
           if uchr.isalpha())



with open('C:/Users/Silas/Downloads/test-wordcount13-20210305T154527Z-001/OneFile/file14.txt', 'w') as outfile:
    for fname in files:
        with open(fname, 'r', errors='ignore') as infile:
            for line in infile:
                outfile.write(line)

f = open("C:/Users/Silas/Downloads/test-wordcount13-20210305T154527Z-001/OneFile/final14.txt", 'w')
with open('C:/Users/Silas/Downloads/test-wordcount13-20210305T154527Z-001/OneFile/file14.txt', 'r') as r:
    for line in sorted(r):
        if only_roman_chars(line):
            f.write(line)