clear all;
close all;
clc;

cd C:\Users\Jangs\Documents\JangProject;

down_N = 3;                            % 다운 샘플링 계수
FILTER_TYPE = 3;                       % 1 == mean 2 == linear 3 == nevile

fprintf('Down_sampling_Level : %d, Filter Type : %d\n',down_N,FILTER_TYPE);

%% 음성신호 읽기 (16bit -> 8bit)
[x_16,fs,bit]=wavread('aeoshang.wav','native'); % read speaker signal
x_16 = x_16(1:4000*100);
% 16 bit -> 8 bit
con_x = int32(x_16) + 32768;
x_8 =  uint8((con_x ./256));  % 캐스팅시 데이터가 손상된다. 1손상 256 -> 255

%% 음성신호 실시간 처리
% Buffer 사용

tic();
%% 음성신호 DowmSampling (구현 시 N 조절가능하도록 만들기) 직접 구현
%아직 안정화 안됨!


total_sample = length(x_8);            % 총샘플링 숫자
ndown_sample =  total_sample/down_N;   % 다운샘플링해서 남는 숫자
down_x = uint8(zeros(1,ndown_sample))';        % 다운샘플링 넣을 배열 할당

for i = 1:ndown_sample
    down_x(i) = x_8(i*down_N);
end

%down_api_x = downsample(x_8,4);
%% White Noise 통과시키기

%% 음성신호 UpSampling (구현 시 N 조절가능하도록 만들기)
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
    % 이거좀 어려울것 같은 느낌인데
    
    %     BUF_FACTOR = 8;
    %     BUF_SIZE =  down_N * BUF_FACTOR;
    %
    %     buffer1 = zeros(BUF_SIZE ,1);
    %     buffer2 = zeros(BUF_SIZE ,1);
    
    %     버퍼쓰지말고 인덱싱으로 하는게 나을 거 같은데?
    fprintf('Nevile_filter\n');
    interp_x =  linear_interpolation_filter(up_x,down_N,ndown_sample);  
end


%% 8bit->int16 확장 모듈 구현

con_x = int32(interp_x) * 256;   % 0 - 65280
con_x = int16(con_x - 32768);

toc();
stem(30000:31000);
player = audioplayer(con_x,fs);
play(player);
% stop(player);

%% 속도테스트 및 오차율 측정,방법





