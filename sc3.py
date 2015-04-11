f = open('TwitterUsers.arff','w')
f.write('@RELATION twitterUsers\n')
for i in range(1,10002):
    f.write('@ATTRIBUTE a'+ str(i) + ' Numeric\n')
f.write('@ATTRIBUTE userType {Politician, Celebrity, NormalUser}\n@DATA\n')
f.close()
