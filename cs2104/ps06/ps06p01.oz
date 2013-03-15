declare
fun {Sieve List}
   case List
   of nil then nil
   [] Head|Tail then List1 in
      List1 = {Filter Tail fun {$ Y} Y mod Head \= 0 end}
      Head|{Sieve List1}
   end
end

declare
fun {Prime N}
   {Sieve {List.number 2 N 1}}
end

{Browse {Prime 20}}