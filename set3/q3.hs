--assume that this is for... all
--
import Data.Ord
import Data.List
--import System.Random, cant find this pacakge

dat = [["CS", "PH", "1","11", "9:30"], ["MA", "ST", "1","11", "7:30"], ["ME", "HU", "2", "11", "9.30"], ["EE", "CM", "2", "11", "7.30"], ["DS", "BS", "3", "11", "9.30"], ["CH", "CU", "3", "11", "7.30"]]

condition x a = a `elem` x

putstr [] = putStr ""
putstr (a:b) = do
	let c = concat (intersperse " " a)
	putStrLn c
	putstr b

teamlist = (permutations tlist)!!143
tlist = ["BS","SM", "CH", "CU", "CS", "DS", "EE", "HU", "MA", "ME", "PH", "ST"]

fixture xe = do 
	if (xe == "all") then do 
		putstr dat
		return ()
	else if (xe `elem` teamlist) then do
		let { a = filter (xe `elem`) dat ; b= head a; c = concat (intersperse " " b)}--b = concat (intersperse " " head a)} --replace find with filter if you want a list of such elements
			--b = a!!0 ++ " " ++ a!!1 ++ a!!2 ++ a!!3 ++ a!!4 ++ "\n"}
		--intercalate " " a
		putStrLn c --instead of print
		return ()
	else do
		putStrLn "There exist no team with name"
		return()

--randomlist = random [1,2,3,4,5]
		--what the heck is this

nextMatch day time = do
	--let temp = read day :: String -- read is to int
	-- but this is neeeded for int to string
	let {tday = show day; ttime = show time; temp = tday}
	-- print "you got this man!"
	-- print temp
	if (tday == "1") then do
		if (time < 0.00) then do
			print "Dude, negative time?  Who taught you that?"
			return ()
		else if (time > 9.30 && time <= 19.30) then do
			let { a = filter (temp `elem`) dat ; b=  a!!1 ; c = concat (intersperse " " b)}
			putStrLn c
			return ()
		else if (time > 19.30) then do
			let {ttday = day + 1; ttemp = show ttday; a = filter (ttemp `elem`) dat ; b=  head a ; c = concat (intersperse " " b)}
			putStrLn c
			return ()
		else do
			let { a = filter (temp `elem`) dat ; b=  head a ; c = concat (intersperse " " b)}
			putStrLn c
			return ()
	else if (tday == "2") then do
		if (time < 0.00) then do
			print "Dude, negative time?  Who taught you that?"
			return ()
		else if (time > 9.30) then do
			let { a = filter (temp `elem`) dat ; b=  a!!1 ; c = concat (intersperse " " b)}
			putStrLn c
			return ()
		else if (time > 19.30) then do
			let {ttday = day + 1; ttemp = show ttday; a = filter (ttemp `elem`) dat ; b=  head a ; c = concat (intersperse " " b)}
			putStrLn c
			return ()
		else do
			let { a = filter (temp `elem`) dat ; b=  head a ; c = concat (intersperse " " b)}
			putStrLn c
			return ()
	else if (tday == "3") then do
		if (time < 0.00) then do
			print "Dude, negative time?  Who taught you that?"
			return ()
		else if (time > 9.30 && time <= 19.30) then do
			let { a = filter (temp `elem`) dat ; b=  a!!1 ; c = concat (intersperse " " b)}
			putStrLn c
			return ()
		else if (time > 19.30) then do
			putStrLn "There are no events after this time!"
		else do
			let { a = filter (temp `elem`) dat ; b=  head a ; c = concat (intersperse " " b)}
			putStrLn c
			return ()
	else do
		print "No matches scheduled on that day"
		return ()
	-- if (temp == "1") then do

	-- else if (temp == 2) then do
	-- 	print temp
	-- else if (temp == 3) then do
	-- 	print temp
	-- else do
	-- 	print temp