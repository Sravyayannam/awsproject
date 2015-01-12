__author__ = 'sravya'
import getGuAndDiameter
import userDefTypes
import findCenterOfGraph
import math
import random
import copy

def calcDistanceToCenter(u_idx,userDistances,resGraph,Gm):
    distance = []
    (center,minx,minValue) = findCenterOfGraph.findCenterOfGraph(resGraph)
    actminx = getGuAndDiameter.findVertexIdx(Gm.Vu,center)
    return userDistances[u_idx][actminx]

def sortAscendingOrder(resGraphList,SD,tList):
    assert isinstance(SD, list)
    idx = []
    len = SD.__len__()
    for x in range(0,len):
        loc = x
        min = SD[x]
        for y in range(x+1,len):
            if(min > SD[y]):
                loc = y
                min = SD[y]
        temp = SD[x]
        SD[x] = SD[loc]
        SD[loc] = temp
        tempGraph = resGraphList[x]
        resGraphList[x] = resGraphList[loc]
        resGraphList[loc] = tempGraph
        tempt = tList[x]
        tList[x] = tList[loc]
        tList[loc] = tempt
        idx.append(loc)

    return (resGraphList,SD,tList,idx)

def ramd(Gm, Ulist, userDistances):
    assert  isinstance(Gm,userDefTypes.graph)
    assert isinstance(Ulist, list)
    sigma = 0.5

    # line 1
    Z = []
    TSD = 0

    # line 2
    Q = Ulist

    # line 3
    while(Q.__len__() != 0):
        # line 4
        resGraphList = []
        SD= []
        tList = []
        delQ = []

        for x in range(0,Q.__len__()):
            t = Q[x]
            assert isinstance(t,userDefTypes.userRequest)
            nIDC = copy.copy(t.mu)
            # line 5
            for v_idx in range (0,Gm.Vu.__len__()):
                v = Gm.Vu[v_idx]
                c = Gm.Cu[v_idx]
                floorKbyMu = math.floor(t.K/t.mu)
                while(c < floorKbyMu):
                    t.mu = t.mu+1
                    floorKbyMu = math.floor(t.K/t.mu)
                if(floorKbyMu == 0):
                    t.mu = nIDC
                    continue
                # line 6
                if c >= floorKbyMu:
                    # line 7
                    last = -1
                    while (last !=0 ):
                        (resGraph,diameter,last) = getGuAndDiameter.getGuAndDiameter(v_idx,Gm,t,t.mu)
                        if last!=0:
                            t.mu = t.mu -1
                    resGraphList.append(resGraph)
                    distance = calcDistanceToCenter(t.Pu,userDistances,resGraph,Gm)
                    # line 8
                    SD.append( (sigma * distance) + ((1-sigma)*diameter))
                    tList.append(x)
        # line 12
        (resGraphList,SD,tList,idx) = sortAscendingOrder(resGraphList,SD,tList)


        # line 13
        len = resGraphList.__len__()
        x = 0
        while(1):
            condition = 0

            thisGraph = resGraphList[x]
            assert  isinstance(thisGraph,userDefTypes.graph)
            for y in range(0,thisGraph.Vu.__len__()):
                thisCu = thisGraph.Cu[y]
                thisVu_idx = getGuAndDiameter.findVertexIdx(Gm.Vu,thisGraph.Vu[y])
                if(Gm.Cu[thisVu_idx] < thisCu):
                    condition = 1
                    break
            if condition == 0:
                delQ.append(tList[x])
                for y in range(0,thisGraph.Vu.__len__()):
                    thisCu = thisGraph.Cu[y]
                    thisVu_idx = thisGraph.Vu[y]-1
                    Gm.Cu[thisVu_idx] = Gm.Cu[thisVu_idx]  - thisCu
                Z.append(thisGraph)
                TSD = TSD + SD[x]
                elem = 0
                delThis = tList[x]
                while(1):
                    if(delThis == tList[elem]):
                        del tList[elem]
                        del SD[elem]
                        del resGraphList[elem]
                        len = len - 1
                    else:
                        elem = elem + 1

                    if(elem == tList.__len__()):
                        x=0
                        break
            else:
                del tList[0]
                del SD[0]
                del resGraphList[0]
                len = len - 1
            if(len==0):
                for i in range(0,delQ.__len__()):
                      Q[delQ[i]] = float("inf")
                i = 0
                while( (Q.__len__() != 0)):
                    if(Q[i] == float("inf")):
                        del Q[i]
                    else:
                        i = i + 1
                    if( i == Q.__len__()):
                        break
                break
    return (Z,TSD)

M = userDefTypes.graph()
numUsers = 1
maxDist  = 50
sizePhotos= 15216
numIDC    = 3

M.Vu = [1, 2, 3, 4, 5]
M.Eu = [[1, 1, 1, 1, 1],[1, 1, 1, 1, 1],[1, 1, 1, 1, 1],[1, 1, 1, 1, 1],[1, 1, 1, 1, 1]]
M.Du = [[0, 20, 30, 40, 50],[20, 0, 10, 30, 40],[30, 10, 0, 40, 50],[40, 30, 40, 0, 10],[50, 40, 50, 10, 0]]
f = open('/home/sravya/location/in.alg','r')
read = f.read()
cuList = read.split()
for x in range(0,cuList.__len__()):
    M.Cu.append(int(cuList[x]))



Ulist = []
#userDistances = [[50,30,40,10,10]]
userDistances = []
for a in range(0,numUsers):
     U = userDefTypes.userRequest()	
     U.Pu = a
     U.K = sizePhotos
     U.mu = numIDC
     Ulist.append(U)
     userDistances.append(random.sample(xrange(1,maxDist+1), 5))


(Z,TSD) = ramd(M,Ulist,userDistances)
f = open('/home/sravya/location/work.alg', 'w')
#f.write(str(Z.__len__())+"\n")
for x in range(0,Z.__len__()):
    Zcurr = Z[x]
    for y in range(0,Zcurr.Vu.__len__()):
        f.write(str(Zcurr.Vu[y]) + "")
    f.write("\n")
   # for y in range(0,Zcurr.Cu.__len__()):
    #    f.write(str(Zcurr.Cu[y]) + " ")
   # f.write("\n")
f.closed
print "done"
