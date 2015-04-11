fin = open('tweets2.txt','r')
fout = open('formattedTweets.txt','w')
lines = fin.readlines()
lines = lines[1:]
tw = ""
for l in lines:    
    if ";;" in l:
        fout.write(tw+"\n")
        tw = l.split(";;")[4]
        tw = tw.strip("\n")
    else:
        tw += l
        tw = tw.strip("\n")
fout.write(tw)
fout.close()
