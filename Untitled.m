a = uint8([1 2 127 6 12 77 88 1]);
aa = uint16(zeros(1, length(a)*4));

for i = 1:8
    for j = 1: 4
        aa((i-1)*4 +j) = a(i); 
    end
end

aaa =  uint16(zeros(1, length(aa)*2));

 for i = 1:length(aa)
        aaa(2*i) = uint16(aa(i))*256;
 end

aaaa =  linear_interpolation_filter(aaa,2,length(aa),2);
aaaa(1) = aaaa(2); 

[N,fpts,mag,wt]=firpmord([5250 9750],[1 0],[0.0005 0.0005],22050);
[q0,err]=firpm(N,fpts,mag,wt);
q0(9)=q0(9)+err;
q0 = single(q0);
h0=firminphase(q0);


aaaaa = conv(h0,aaaa);


