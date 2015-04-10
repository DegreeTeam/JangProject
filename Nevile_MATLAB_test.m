
a = int32([0 0 0 10 0 0 0 34 0 0 0 128 0 0 0 198 0 0 0 242]);
len_a = length(a);

Cur = 4;
down_N = 4;
factor = 5;

buffer1 = zeros(1,factor);
buffer2 = zeros(1,factor);

for  ii= Cur:down_N:down_N*factor  % Cur의   이동 x의 인덱스
    for jj = down_N-1:-1:1 % 빈칸 채우기 이게 x      i - j  <  - 해당 인덱스
        a(ii-jj); % INDEX Check용
        for i = 1:factor
            buffer2(1) = a(i*4);   
            add_index = 1;
            for j = i: -1 : 2
                
             gp = buffer1(i-j+1);
             gc = buffer2(i-j+1);
             xe = ((i)*Cur);
             xs = ((i - add_index)*Cur);
             x = (ii-jj);         
             
             % sprintf('1: %d, 2: %d, 3: %d, 4: %d, 5: %d',gp, gc,xe, xs,x)         
             a(ii-jj) = int32((((x - xs)*gc - (x -xe)*gp) / (xe - xs)));            
             buffer2(i-j+2) = a(ii-jj);          
            add_index = add_index+1;
            end
            
            for j = 1:i
                buffer1(j) = buffer2(j);
            end
        end 
    end
end
