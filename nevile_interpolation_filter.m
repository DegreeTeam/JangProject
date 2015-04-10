%% 2015.4.10
% nevile interpolation filter
% argument1 =  what?
% argument2 =  How Level?
% argument3 =  length
% Output is Filtered argument1

% 4.10 Unstable state
function vect_result = nevile_interpolation_filter(vect_X,down_Level,ndown_sample)
% ��� ���� ��� factor�� ������ ��
Cur = down_Level;
factor = 5;

% ���� �Ҵ�
vect_result = vect_X;
buffer1 = zeros(1,factor);
buffer2 = zeros(1,factor);
Divider = ndown_sample/(down_Level *factor);
% down_Level�� ���� ó��
for itr = 1: Divider 
    for  ii = itr*Cur:down_N:itr*(down_N*factor)  % Cur��   �̵� x�� �ε���
        for jj = down_N-1:-1:1 % ��ĭ ä��� �̰� x      i - j  <  - �ش� �ε���
            a(ii-jj); % INDEX Check��
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
end
end