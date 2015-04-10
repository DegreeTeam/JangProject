%% 2015.4.3 
% linear interpolation filter
% argument1 =  what?
% argument2 =  How Level?
% argument3 =  length
% Output is Filtered argument1  

function vect_result = linear_interpolation_filter(vect_X,down_Level,ndown_sample)
% 공간 할당
vect_result = vect_X;

% down_Level에 따른 처리
for i = 1:(down_Level-1)
   for j = 1:(ndown_sample-1)        
          val = uint16(vect_X(down_Level*(j+1))) - uint16(vect_X(down_Level*j));  
          val = val * i;
          val = val/down_Level;
          vect_result(down_Level *j + i) = vect_X(down_Level*j) + uint8(val);
   end
end

%stem(vect_result(30000:31000));
end