clear all;
close all;
clc;

cd C:\Users\Jangs\Documents\JangProject;

down_N = 3;                            % �ٿ� ���ø� ���
FILTER_TYPE = 3;                       % 1 == mean 2 == linear 3 == nevile

fprintf('Down_sampling_Level : %d, Filter Type : %d\n',down_N,FILTER_TYPE);

%% ������ȣ �б� (16bit -> 8bit)
[x_16,fs,bit]=wavread('aeoshang.wav','native'); % read speaker signal
x_16 = x_16(1:4000*100);
% 16 bit -> 8 bit
con_x = int32(x_16) + 32768;
x_8 =  uint8((con_x ./256));  % ĳ���ý� �����Ͱ� �ջ�ȴ�. 1�ջ� 256 -> 255

%% ������ȣ �ǽð� ó��
% Buffer ���

tic();
%% ������ȣ DowmSampling (���� �� N ���������ϵ��� �����) ���� ����
%���� ����ȭ �ȵ�!


total_sample = length(x_8);            % �ѻ��ø� ����
ndown_sample =  total_sample/down_N;   % �ٿ���ø��ؼ� ���� ����
down_x = uint8(zeros(1,ndown_sample))';        % �ٿ���ø� ���� �迭 �Ҵ�

for i = 1:ndown_sample
    down_x(i) = x_8(i*down_N);
end

%down_api_x = downsample(x_8,4);
%% White Noise �����Ű��

%% ������ȣ UpSampling (���� �� N ���������ϵ��� �����)
%up_api_x = upsample(down_x,down_N);

up_x = uint8(zeros(1,total_sample))';
for i = 1:ndown_sample
    up_x(down_N*i) = down_x(i);
end

%% interpolation
if(FILTER_TYPE == 1)
    % Mean filter
    fprintf('Mean_filter\n');
    interp_x =  mean_interpolation_filter(up_x,down_N,ndown_sample);
end
if(FILTER_TYPE == 2)
    % Linear filter
    fprintf('Linear_filter\n');
    interp_x =  linear_interpolation_filter(up_x,down_N,ndown_sample);
end
if(FILTER_TYPE == 3)
    % Nevile filter
    % �̰��� ������ ���� �����ε�
    
    %     BUF_FACTOR = 8;
    %     BUF_SIZE =  down_N * BUF_FACTOR;
    %
    %     buffer1 = zeros(BUF_SIZE ,1);
    %     buffer2 = zeros(BUF_SIZE ,1);
    
    %     ���۾������� �ε������� �ϴ°� ���� �� ������?
    fprintf('Nevile_filter\n');
    interp_x =  linear_interpolation_filter(up_x,down_N,ndown_sample);  
end


%% 8bit->int16 Ȯ�� ��� ����

con_x = int32(interp_x) * 256;   % 0 - 65280
con_x = int16(con_x - 32768);

toc();
stem(30000:31000);
player = audioplayer(con_x,fs);
play(player);
% stop(player);

%% �ӵ��׽�Ʈ �� ������ ����,���





