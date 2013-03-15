partition([], _, [], []).
partition([Head|Tail],Pivot,Smaller,Bigger) :-
	((Head < Pivot)
	-> Smaller = [Head|Rest], partition(Tail,Pivot,Rest,Bigger)
	; Bigger = [Head|Rest], partition(Tail,Pivot,Smaller,Rest)
	).

quicksort([],[]).
quicksort([Head|Tail],Sorted) :-
	partition(Tail,Head,Smaller,Bigger),
	quicksort(Smaller,SortedSmaller),
	quicksort(Bigger,SortedBigger),
	append(SortedSmaller,[Head|SortedBigger],Sorted).