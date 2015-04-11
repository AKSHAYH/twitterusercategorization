from stemming.porter2 import stem
f3 = open('google-10000-english.txt','r')
f4 = open('stemmed-1000-words.txt','w')

#print len(f3.readlines())
for word in f3.readlines():
    word = word.strip('\n')
    f4.write(stem(word)+'\n')
f3.close()
f4.close()
