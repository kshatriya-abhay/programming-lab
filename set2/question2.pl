starter("Corn Tikki").
starter("Tomato Soup").
starter("Chilli Paneer").
starter("Crispy chicken").
starter("Papdi Chaat").
starter("Cold Drink").

maindish("Kadhai Paneer with Butter / Plain Naan").
maindish("Veg Korma with Butter / Plain Naan").
maindish("Murgh Lababdar with Butter / Plain Naan").
maindish("Veg Dum Biryani with Dal Tadka").
maindish("Steam Rice with Dal Tadka").

desert("Ice-cream").
desert("Malai Snadwich").
desert("Rasmalai").

nutrient("Corn Tikki",30).
nutrient("Tomato Soup",20).
nutrient("Chilli Paneer",40).
nutrient("Crispy chicken",40).
nutrient("Papdi Chaat",20).
nutrient("Cold Drink",20).

nutrient("Kadhai Paneer with Butter / Plain Naan",50).
nutrient("Veg Korma with Butter / Plain Naan",40).
nutrient("Murgh Lababdar with Butter / Plain Naan",50).
nutrient("Veg Dum Biryani with Dal Tadka",50).
nutrient("Steam Rice with Dal Tadka",40).

nutrient("Ice-cream",20).
nutrient("Malai Snadwich",30).
nutrient("Rasmalai",10).

menu(hungry,1,1,1).
menu(not_so_hungry,0,1,1).
menu(not_so_hungry,1,1,0).
menu(diet,1,0,0).
menu(diet,0,1,0).
menu(diet,0,0,1).

% find_items checks if given statement satisfies status conditions (menu) and then finds the relevant items
find_items(A,X,Y,Z) :- menu(A,X,Y,Z),find(A,X,Y,Z).

% find triggers the getlist functions if we are on a diet, otherwise gets one item of desired category.

find(hungry,_,_,_) :- starter(A),maindish(B),desert(C),write("Items: "),write(A),write(", "),write(B),write(", "),write(C).
find(not_so_hungry,1,1,0) :-  starter(A),maindish(B),write("Items: "),write(A),write(", "),write(B).
find(not_so_hungry,0,1,1) :-  maindish(B),desert(C),write("Items: "),write(B),write(", "),write(C).

find(diet,1,0,0) :- getlist_starter(Items,40), Items \= [], write("Items: "),write(Items).
find(diet,0,1,0) :- getlist_maindish(Items,40), Items \= [], write("Items: "),write(Items).
find(diet,0,0,1) :- getlist_desert(Items,40), Items \= [], write("Items: "),write(Items).

% Below functions recursively find if there are multiple possible items to eat within the budget (40).

getlist_starter(Items, N) :- starter(A), nutrient(A,B), B =< N, NextN is N - B, getlist_starter(NextList,NextN), not(member(A,NextList)), append(NextList,[A],Items).
getlist_starter(Items, _) :- Items = [].

getlist_maindish(Items, N) :- maindish(A), nutrient(A,B), B =< N, NextN is N - B, getlist_maindish(NextList,NextN), not(member(A,NextList)), append(NextList,[A],Items).
getlist_maindish(Items, _) :- Items = [].

getlist_desert(Items, N) :- desert(A), nutrient(A,B), B =< N, NextN is N - B, getlist_desert(NextList,NextN), not(member(A,NextList)), append(NextList,[A],Items).
getlist_desert(Items, _) :- Items = [].
