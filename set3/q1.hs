import Data.Ord
import Data.List


main = do putStrLn "This Works!"
asd = do putStrLn "Called asd!"

indsum:: [[Integer]] ->[Integer]
indsum [[]] = [0]
indsum [] = []
indsum (a:b) = sum a : indsum b
prodsum a = (product.indsum)(a)
summ = [[1,2,3,4,5], [1,2,3], [10] ]


greatest f x = maximumBy (comparing f) x

data List qw = Empty | Cons qw (List qw) deriving (Show, Read, Eq, Ord)

toList [] = Empty
toList (headd:taill) = Cons headd (toList taill) 

toHaskellList Empty = []
toHaskellList (Cons headd taill) = (headd:toHaskellList taill)
--learn how ordering works
--https://stackoverflow.com/questions/40209169/maximizing-according-to-a-function

-- indprod:: [Int] -> Int
-- indprod [] = (Int) 0
-- indprod (a) = product
--the definitions kill...
--fails in case of [] as an input, since that doesn't satisfy the constraints
--can have a parser that does the same...
-- sumprod a = (indprod.indsum)(a)

--prodd = indprod [[1,2,3,4,5], [1,2,3]]
--sumprodtest = sumprod [[1,2,3,4,5], [1,2,3], []]


-- factorial 0  = 1
-- factorial n = n*factorial(n-1)

-- fact = factorial 5
--maximize f x y = argmax f [x,y]




-- part1 :: [[int]] -> [int]
-- part1([]:b) = []
-- part1(a:b) = product a : part1(b)

--data = []

-- getlistprod :: [int] -> int
-- getlistprod(a) = product a

-- summ = sum [1,2,3,4,5]