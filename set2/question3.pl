road(g1,g5,4).
road(g2,g5,6).
road(g3,g5,8).
road(g1,g6,10).
road(g2,g6,9).
road(g3,g6,3).
road(g4,g6,5).
road(g5,g7,3).
road(g5,g10,4).
road(g5,g11,6).
road(g5,g12,7).
road(g5,g6,7).
road(g5,g8,9).
road(g6,g8,2).
road(g6,g12,3).
road(g6,g11,5).
road(g6,g10,9).
road(g7,g10,2).
road(g7,g11,5).
road(g7, g12, 7).
road(g7, g8, 10).
road(g8, g9, 3).
road(g8, g12, 10).
road(g8, g12, 10).
road(g8, g11, 4).
road(g8, g10, 8).
road(g10, g15, 5).
road(g10, g11, 2).
road(g10, g12, 5).
road(g11, g15, 4).
road(g11, g13, 5).
road(g11, g12, 4).
road(g12, g13, 7).
road(g12, g14, 5).
road(g15, g13, 4).
road(g13, g14, 8).
road(g14, g17, 5).
road(g14, g18, 4).
road(g17, g18, 8).

% Part A

% Prints paths starting from g1, g2, g3, g4 respectively.
writeall() :- allpaths(g1);allpaths(g2);allpaths(g3);allpaths(g4).
% starts the traversal from the gate Y
allpaths(Y) :- road(Y,A,_),traverse(X,Y,A),write("Path: "),write(X).

% Termination condition for recursion, end on gate 17.
traverse(X,_,g17) :- X = [g14,g17].
% Recursion to store a valid path.
traverse(X,A,B) :- road(B,C,_), C \= g9, C \= g7, C \= g18, traverse(NextX,B,C), append([A],NextX,X).

% Part B

% To find minimum of a list.
listmin([L|Ls],Min) :- foldl(getmin,Ls,L,Min).
getmin(X,Y,Min) :- Min is min(X,Y).

% Similar to part A, calculations path length starting from g1,g2,g3,g4 respectively
% Rest of the approach is also similar.
getpath(V) :- startfrom(V,g1);startfrom(V,g2);startfrom(V,g3);startfrom(V,g4).
startfrom(V,Y) :- road(Y,A,N), recurse_path(NextV,_,Y,A),V = NextV+N.
recurse_path(V,X,A,B) :- road(B,C,N), C \= g9, C \= g7, C \= g18, recurse_path(NextV,NextX,B,C), append([A],NextX,X), V = NextV+N.
recurse_path(V,X,_,g17) :- X = [g14,g17], V = 0.

% Part C

% Similar to part A, finding the path(s) of minimum length starting from g1,g2,g3,g4 respectively.
% Rest of the approach is also similar.

listpath(V,X) :- start(V,X,g1);start(V,X,g2);start(V,X,g3);start(V,X,g4).
start(V,X,Y) :- road(Y,A,N), NextV is V-N, recurse(NextV,X,Y,A).
recurse(V,X,A,B) :- road(B,C,N), C \= g9, C \= g7, C \= g18, N =< V, NextV is V-N,recurse(NextV,NextX,B,C), append([A],NextX,X).
recurse(V,X,_,g17) :- X = [g14,g17], V = 0.

optimum() :- findall(V,getpath(V),List), listmin(List,Min), listpath(Min,X), write(X).

valid(List) :- List \= [], check_start(List).
check_start([A|List]) :- A == g1, [B|Listx] = List, vroad(A, B, _), Listx \= [], check_list(B,Listx) ; A == g2, [B|Listx] = List, vroad(A, B, _), Listx \= [], check_list(B,Listx); A == g3, [B|Listx] = List, vroad(A, B, _), Listx \= [], check_list(B,Listx); A == g4, [B|Listx] = List, vroad(A, B, _), Listx \= [], check_list(B,Listx).
check_list(_,[g17|List]) :- List == [].
check_list(A,[B|List]) :- vroad(A,B,_), check_list(B,List).
vroad(A, B, _):- road(A,B, _); road(B, A, _).