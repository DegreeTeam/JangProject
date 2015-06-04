%% 2015.4.3
% Mean interpolation filter
% argument1 =  what?
% argument2 =  How Level?
% argument3 =  length
% argument4 =  datatype
% Output is Filtered argument1
%  Up to Down Sampling Level 5
function vect_result = mean_interpolation_filter(vect_X,down_Level,ndown_sample,datatype)
% 공간 할당
vect_result = vect_X;
% down_Level에 따른 처리
for i = 1:(down_Level-1)
    for j = 1:(ndown_sample-1)
        if(datatype == 1)
        % uint8 의 경우 보간법
         vect_result(down_Level *j+i) = uint8((uint16(vect_X(down_Level*j)) + uint16(vect_X(down_Level*(j+1))))/2);
        end
        if(datatype == 3)
        % float 의 경우 보간법
        vect_result(down_Level *j+i) = (vect_X(down_Level*j) + vect_X(down_Level*(j+1)))/2;
        end
    end
end

end