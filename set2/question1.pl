%checks if a given substring S of length 1 or 2 is valid i.e. represents an alphabet
string_check(S):- atom_number(S, QW), QW < 27, QW > 0.   	
%does test1 and test2, each for 1 character and 2 character tess
tester(S, X):- string_length(S, T1), T1>1,test1(S ,X1), test2(S, X2), X is X1 + X2.	
%tests only on test1 if length is exactly 1
tester(S, X):- string_length(S, T1), T1=:=1,test1(S ,X1), X is X1.			
%base case, i.e. string is empty
tester("", 1).										
%breaks the string into two parts, 1 char length and remaining.  Operates only if one character length is valid.  Calls tester on the remaining part
test1(S, X):- string_length(S,T1),T1 > 0, sub_string(S, 0, 1, A1, Subs1), string_check(Subs1),sub_string(S, 1, A1, _, Subs2),
  tester(Subs2,X).
%similar to the notion of test2
test2(S, X):- string_length(S, T2), T2 > 1,sub_string(S, 0,2, A1, Subs1), string_check(Subs1),sub_string(S, 2, A1,_, Subs2), tester(Subs2, X).



