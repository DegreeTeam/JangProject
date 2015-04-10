%% 2015.4.3
% Mean interpolation filter
% argument1 =  what?
% argument2 =  How Level?
% argument3 =  length
% Output is Filtered argument1
%  Up to Down Sampling Level 5
function vect_result = mean_interpolation_filter(vect_X,down_Level,ndown_sample)
% 공간 할당
vect_result = vect_X;
% down_Level에 따른 처리
for i = 1:(down_Level-1)
    for j = 1:(ndown_sample-1)
        vect_result(down_Level *j+i) = uint8((uint16(vect_X(down_Level*j)) + uint16(vect_X(down_Level*(j+1))))/2);
    end
end

end