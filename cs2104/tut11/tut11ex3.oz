declare
fun {DConsume ?Xs A Limit}
   if Limit>0 then
      local X Xr in Xs=X|Xr {DConsume Xr A+X Limit-1} end
   else A end
end

proc {DProduce N Xs}
   case Xs of X|Xr then
      X=N*N
      {DProduce N+1 Xr}
   end
end

local Xs S in
   thread {DProduce 0 Xs} end
   thread S={DConsume Xs 0 5} end
   {Browse S}
end

declare
proc {Buffer N Xs Ys}
   fun {Startup N ?Xs}
      if N==0 then Xs
      else Xr in Xs=_|Xr {Startup N-1 Xr} end
   end
   proc {AskLoop Ys ?Xs ?End}
      case Ys of Y|Yr then Xr End2 in
	 Xs=Y|Xr % get element from buffer
	 End=_|End2 % replenish the buffer
	 {AskLoop Yr Xr End2}
      [] nil then End=nil end
   end
   End={Startup N Xs}
in
   {AskLoop Ys Xs End}
end

local Xs Ys in
   thread {DProduce 0 Xs} end
   thread {Buffer 3 Xs Ys} end
   thread {DConsume Ys A Limit} end
   {Browse Ys}
end

