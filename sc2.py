from stemming.porter2 import stem
from datetime import datetime
import re,time

types = ['HealthCare' ]
f3 = open('wordlist.txt','r')
f2 = open('TweetFeatureshajj.arff','w')
f4 = open('temp2.txt','w')
f2.write('@RELATION twitterUsers\n')
for i in range(1,1918):
    f2.write('@ATTRIBUTE a'+ str(i) + ' Numeric\n')
f2.write('@ATTRIBUTE userType {Politician,Celebrity,Entrepreneur,Journalism,Science,HealthCare}\n@DATA\n')
vocabList = f3.readlines()
wordHash = dict()
i = 0
for word in vocabList:
    word = word.strip('\n')
    wordHash[word] = i
    i += 1

for userType in types:
    f1 = open('TestTweets2.txt','r')   
    
    def extractFeatures(strIn):
        featureList = [0]*len(vocabList)
        strIn = strIn.lower()
        strIn = re.sub('<[^<>]>',' ',strIn)
        strIn = re.sub('[0-9]+','number',strIn)
        strIn = re.sub('(http|https)://[^\s]*', 'httpaddr', strIn)
        strIn = re.sub('[^\s]+@[^\s]+', 'replyaddr', strIn)
        strIn = re.sub('[^\s]+#[^\s]+', 'hashtg', strIn)
        strIn = re.sub('[$]+', 'dollar', strIn)
        strIn = re.sub('[@$/#.-:&*+=[]?!(){},\'\">_<;%]', '', strIn)
        strIn = re.sub("'",'',strIn)
        strIn = strIn.replace('\n',' ')    
        for word in strIn.split(' '):    
            stemmedWord = stem(word)        
            if stemmedWord in wordHash.keys():            
                featureList[wordHash[stemmedWord]] = 1    
            
        return featureList

    def timeSeriesFeatures(x,y):
        AverageTweetsPerDay = y[0]
        MaxTweetsPerDay = y[0]
        MinTweetsPerDay = y[0]
        MaxChange = 0
        MinChange = 1000
        AverageChange = 0
        
        for i in range(1,len(y)):
            AverageTweetsPerDay += float(y[i])
            MaxTweetsPerDay = max(MaxTweetsPerDay,y[i])
            MinTweetsPerDay = min(MinTweetsPerDay,y[i])
            MaxChange = max(MaxChange,y[i]-y[i-1])
            MinChange = min(MinChange,y[i]-y[i-1])
            AverageChange += y[i]-y[i-1]

        try:
            AverageTweetsPerDay = AverageTweetsPerDay/(x[-1]-x[0])
            AverageChange = AverageChange/(x[-1]-x[0])    
        except ZeroDivisionError:        
            pass
        StandardDev = sum([(i-AverageTweetsPerDay)**2 for i in y ])
        MedianTweets = sorted(y)[len(y)/2]    
        return [AverageTweetsPerDay, MaxTweetsPerDay, MinTweetsPerDay, StandardDev, MedianTweets, MaxChange, MinChange, AverageChange]

    def writeFeatures(array, fileName):
        f2.write(re.sub('[\[\],]','',str(array))+ ' ' + userType + '\n')
        return

    fileContent = f1.read()
    for userDet in fileContent.split('#####'):
        tweets = userDet.split(';;;;;')
        tweets = tweets[1:]
        tweetStr = ''
        favCount = 0
        retweetCount = 0
        sensitiveCount = 0

        dateList = []
        
        for tw in tweets:
            if ';;' in tw:            
                tweetDate = time.strptime(tw.split(';;')[0],"%a %b %d %H:%M:%S IST %Y")
                dateList.append(tweetDate            )
                tweetStr += tw.split(';;')[4]
                favCount += int(tw.split(';;')[1])
                retweetCount += int(tw.split(';;')[2])
                sensitiveCount += 1 if tw.split(';;')[3]==True  else 0
        if tweetStr != '':
            tweetStr = tweetStr.replace('\n','')
            feat = extractFeatures(tweetStr)
            followers = int(userDet.split(';;')[3])
            friends = int(userDet.split(';;')[4])
            fav = int(userDet.split(';;')[2])
            rep = float(followers)/(friends+followers)
            feat += [friends, followers, fav, rep, favCount, retweetCount, sensitiveCount]
            
            xx = []
            yy = []
            cnt = 1    
            for i in range(1,len(dateList)):        
                if dateList[i].tm_mday == dateList[i-1].tm_mday and dateList[i].tm_mon == dateList[i-1].tm_mon and dateList[i].tm_year == dateList[i-1].tm_year:
                    cnt += 1
                else:
                    xx.append((datetime.fromtimestamp(time.mktime(dateList[0]))-datetime.fromtimestamp(time.mktime(dateList[i-1]))).days)
                    yy.append(cnt)
                    cnt = 1
            xx.append((datetime.fromtimestamp(time.mktime(dateList[0]))-datetime.fromtimestamp(time.mktime(dateList[-1]))).days)
            yy.append(cnt)
            
            feat.append(timeSeriesFeatures(xx,yy))
            #print len(feat)
            writeFeatures(feat,f2)
            f4.write(userDet.split(";;")[0] + str(len(tweets))+"\n")
            #break       
            
    f1.close()
    
f2.close()    
f3.close()
f4.close()
