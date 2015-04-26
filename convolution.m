%% CONVOLUTION  È®ÀÎ CODE

f = single([0.2 0.3 0.4 0.5 0.8]);
data = single([0.3 1.5 1.7 3.4 3.4 99 1.22 9.66]);
resultdata = ones(1,8192);
resultdata(1:1024) = data(1)*resultdata(1:1024); 
resultdata(1024 + 1:2*1024) = data(2)*resultdata(1024 + 1:2*1024); 
resultdata(2*1024 + 1:3*1024) = data(3)*resultdata(2*1024 + 1:3*1024); 
resultdata(3*1024 + 1:4*1024) = data(4)*resultdata(3*1024 + 1:4*1024); 
resultdata(4*1024 + 1:5*1024) = data(5)*resultdata(4*1024 + 1:5*1024); 
resultdata(5*1024 + 1:6*1024) = data(6)*resultdata(5*1024 + 1:6*1024); 
resultdata(6*1024 + 1:7*1024) = data(7)*resultdata(6*1024 + 1:7*1024); 
resultdata(7*1024 + 1:8*1024) = data(8)*resultdata(7*1024 + 1:8*1024); 

result1 = conv(resultdata,f)
sum(result1(1:1024))
sum(result1(1*1024 + 1:2*1024))
sum(result1(2*1024 + 1:3*1024))
sum(result1(3*1024 + 1:4*1024))
sum(result1(4*1024 + 1:5*1024))
sum(result1(5*1024 + 1:6*1024))
sum(result1(6*1024 + 1:7*1024))
sum(result1(7*1024 + 1:8*1024))

lenf = length(f);
lendata = length(data); 

nconv = lenf + lendata -1;
result2 = single(zeros(1,nconv)); 

for i = 1:nconv
   i1 = i;
   tmp = 0.0;
   for j = 1:lenf
       if(i1>=1 && i1<=lendata)
           tmp = tmp + (data(i1)*f(j));
       end
       i1 = i1 -1;
       result2(i) = tmp;
   end
end




