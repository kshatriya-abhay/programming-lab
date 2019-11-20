import Data.List
--let row = permutations [1,2,3,4]
findall row = [[row !! a,row !! b, row!!c, row !! d] | a <- [0..23], b <- [0..23], c<-[0..23], d<-[0..23], a/=b,a/=c,a/=d,b/=c,b/=d,c/=d, check [row !! a,row !! b, row!!c, row !! d]]
check sud = checkcol sud && checkbl sud
checkcol sud = (checkcoli sud 0) && (checkcoli sud 1) && (checkcoli sud 2) && (checkcoli sud 3)
checkcoli sud j = not (hasDuplicates [(sud !! i !! j) | i<-[0..3]])
checkbl sud = (checkbli sud 0 0) && (checkbli sud 0 2) && (checkbli sud 2 0) && (checkbli sud 2 2)
checkbli :: [[Int]]->Int->Int->Bool
checkbli sud i j = not (hasDuplicates [(sud !! (i+a) !! (j+b)) | a<-[0..1], b<-[0..1] ])
hasDuplicates :: [Int]->Bool
hasDuplicates xs = length (nub xs) /= length xs
--solve sud = findit sud (permutations [1,2,3,4])
findit :: [[Int]]->[[[Int]]]->IO()
findit sud alls = if((length sollist) == 1) then (print (sollist !! 0)) else putStrLn "Zero or Multiple Solutions"
	where
	sollist = [ (alls !! i) | i <-[0..277], comp sud (alls!!i) ]
comp sud sol = listand(map listand(zipWith (zipWith (isok)) sud sol))
isok :: Int->Int->Bool
isok a b = if(a == 0) then True
   else (a == b)
listand list = foldl1 (&&) list

-- input: findit *2d list input* (findall(permutations [1,2,3,4]))