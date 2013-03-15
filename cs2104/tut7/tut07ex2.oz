declare
fun {Pascal N K}
   if N==0 orelse N==K then 1 else {Pascal N-1 K}+{Pascal N-1 K-1}
   end
end

{Browse {Pascal 10 5}}