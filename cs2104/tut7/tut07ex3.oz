declare
A = {NewArray 0 99 0}
proc {RandInit I}
   if I < 100 then
      {Put A I {OS.rand}}
      {RandInit I+1}
   else skip
   end
end

{RandInit 0}

fun {MinArray I J}
   M = {NewCell I} Helper in
   proc {Helper K}
      if K =< J then
	 if {Get A @M} > {Get A K} then
	    M := K
	 end
	 {Helper K+1}
      else skip
      end
   end
   {Helper I}
   @M
end


proc {Sort}
   Helper in
   proc {Helper K}
      if K<100 then
	    M = {MinArray K 99}
	    T = {Get A M}
	 in
	    {Put A M {Get A K}}
	    {Put A K T}
	    {Helper K+1}
      else skip
      end
   end
   {Helper 0}
end

proc {PrintArray K}
   if K<100 then
      {Browse {Get A K}}
      {PrintArray K+1}
   else skip
   end
end

{Sort}
{PrintArray 0}