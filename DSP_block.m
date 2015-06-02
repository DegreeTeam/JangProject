clear all;
close all;
clc;

cd C:\Users\Jangs\Documents\JangProject;

TESTCASE = 1;
down_N = 6;                            % �ٿ� ���ø� ���
FILTER_TYPE = 3;                       % 1 == mean, 2 == linear, 3 == nevile
datatype = 1;                          % 1 == uint8, 2 == int16, 3 == float
BPFtype = 3;                           % 1 == AM Radio 2 == FM Radio 3 == NTSC TV Audio 

fprintf('Down_sampling_Level : %d, Filter Type : %d, Data type : %d\n',down_N,FILTER_TYPE, datatype);

%% ������ȣ �б� (16bit -> 8bit)
if(TESTCASE == 1)
    [x_16,fs,bit]=wavread('testcase1.wav','native'); % read speaker signal
end
if(TESTCASE == 2)
    [x_16,fs,bit]=wavread('testcase2.wav','native'); % read speaker signal
end
if(TESTCASE == 3)
    [x_16,fs,bit]=wavread('testcase3.wav','native'); % read speaker signal
end
if(TESTCASE == 4)
    [x_16,fs,bit]=wavread('testcase4.wav','native'); % read speaker signal
end
if(TESTCASE == 5)
    [x_16,fs,bit]=wavread('testcase5.wav','native'); % read speaker signal
end

x_16 = x_16(1:32);
% 16 bit -> 8 bit
tic();
con_x = int32(x_16) + 32768;
x_8 =  uint8((con_x ./256));  % ĳ���ý� �����Ͱ� �ջ�ȴ�. 1�ջ� 256 -> 255
if(datatype == 3)
    x_float = single(x_16)/32768;
end

x_16 = int16(x_8);
%% ������ȣ �ǽð� ó��
% Buffer ���


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
if(datatype == 1)
    up_x = uint8(zeros(1,total_sample))';
    for i = 1:ndown_sample
        up_x(down_N*i) = down_x(i);
    end
end

if(datatype == 3)
    up_x = single(zeros(1,total_sample))';  % float data�� ����
    for i = 1:ndown_sample
        up_x(down_N*i) = (single(down_x(i)) - 128)/128;
    end
end



%% interpolation
if(FILTER_TYPE == 1)
    % Mean filter
    fprintf('Mean_filter\n');
    interp_x =  mean_interpolation_filter(up_x,down_N,ndown_sample,datatype);
end
if(FILTER_TYPE == 2)
    % Linear filter
    fprintf('Linear_filter\n');
    interp_x =  linear_interpolation_filter(up_x,down_N,ndown_sample,datatype);
end
if(FILTER_TYPE == 3)
    % Nevile filter
    fprintf('Nevile_filter\n');
    interp_x =  nevile_interpolation_filter(up_x,down_N,ndown_sample);
end

toc();

if(datatype == 1)
    %% 8bit->int16 Ȯ�� ��� ����
    
    con_x = int32(interp_x) * 256;   % 0 - 65280
    con_x = int16(con_x - 32768);
    
    %%  Data Check�� ���� (��ü ���ü��� ����)
    diff= sum(abs(x_8  - interp_x));
    fprintf('Error rate : %d\n', diff/total_sample);
end


if (datatype == 3)
    con_x = interp_x;
    %%  Data Check�� ���� (��ü ���ü��� ����)
    diff= sum(abs(x_float  - con_x));
    fprintf('Error rate : %s\n', diff/total_sample);
end

%% BPF �����Ű�� 
% tic();
% result_x = conv(con_x,h0); 
% toc();
%  %%  Data Check�� ���� (��ü ���ü��� ����)
%     diff= sum(abs(x_float  - result_x(1:total_sample)));
%     fprintf('(After_BPF) Error rate : %s\n',diff/total_sample);
% 
% figure(2);

%stem(con_x(30000:31000))
% player = audioplayer(con_x,fs);
% play(player);
% % stop(player);

%% �ӵ��׽�Ʈ �� ������ ����,���





