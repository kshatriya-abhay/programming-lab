import System.IO
import Data.List
joinstr :: [String] -> String
joinstr list = (head list) ++ (list !! 1)
substring :: String -> [String]
substring [] = []
substring xs = subs xs ++ substring (tail xs)
   where
      subs xs = foldl step [] xs
	  step [] a = [[a]]
	  step acc a = (head acc ++ [a]) : acc

listall :: [String]->[String]
listall list = calc(tail( substring (joinstr list)))

countlist :: [String]->Int
countlist list = length(listall list)

countlist :: [String]->[String]
calc str = [ (str !! x , str !! y) | x <-[0 .. n-1],y <- [x+1 .. n-1], check (str!!x) (str!!y) ]
   where n = length(str)

check :: String->String->bool
check str1 str2 = sort(str1) == sort(str2)