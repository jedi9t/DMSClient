package ui;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.TrackInfo;
import android.media.TimedText;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;
import java.util.List;

import helper.PreferenceManager;
import tvdms.Dms;
import util.CommonUtil;
import util.FileUtil;
import widget.RandomTextView;


public class MyMediaPlayer {

	private SurfaceView sv_video;
	private SurfaceHolder surfaceHolder;
	// ������Ƶ����
	private static MediaPlayer mediaPlayer;
	private String path;
	private boolean PasueFlag = false;
	// ��¼��ͣ���ŵ�λ��
	private int currentPosition = 0;
	// ���Ű�ť�Ŀؼ�
	private LinearLayout video_layout;
	// ����һ���طŵļ�¼λ��
	private int backPosition = 0;
	private Context context;
	List<String> pathlist;
	String srtPath;
	String txtPath;
	int _tag;

	String TAG = "MyMediaPlayer";

	RandomTextView rantv;

/*	public MyMediaPlayer(SurfaceView _sv_video, String _path) {
		sv_video = _sv_video;
		path = _path;
		System.out.print(path);
		// ͨ���ؼ�����ȡSurfaceHolder
		surfaceHolder = sv_video.getHolder();
		// �ص�����
		surfaceHolder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				play();
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}
		});
		backPosition = 0;
	}*/
 
	public MyMediaPlayer(Context context, SurfaceView _sv_video, List<String> _pathlist) {
		this.pathlist = _pathlist;
		if (pathlist != null && pathlist.size() > 0) {
			_tag = 0;
			srtPath = null;
			txtPath = null;

		/*	if (pathlist.get(_tag).getRefpath() != null && !"".equals(pathlist.get(_tag).getRefpath())) {
				if (pathlist.get(_tag).getRefpath().endsWith("txt")) {
					txtPath = pathlist.get(_tag).getRefpath();
					playTxt(txtPath);
				} else {
					srtPath = pathlist.get(_tag).getRefpath();
				}
			}*/
			
			initMediaPlayer(context, _sv_video, pathlist.get(_tag));
		}
	}

	public void initMediaPlayer(Context context, SurfaceView _sv_video, String path) {
		Log.i("mytest", "MyMediaPlayer.initMediaPlayer   path=" + path);
		this.context = context;
		PreferenceManager.init(context);
		this.path=path;
		sv_video = _sv_video;
		System.out.print(path);
		// ͨ���ؼ�����ȡSurfaceHolder
		surfaceHolder = sv_video.getHolder();
		// �ص�����
		surfaceHolder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// PreferenceManager.getInstance().setPlayCount(_programId,
				// PreferenceManager.getInstance().getPlayCount(_programId)+1);
				play();
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}
		});
		backPosition = 0;
	}

	// ��ȡ��Ƶ��ǰ·��
	public String path() {
		return path;

	}

	/*public void play(String _path) {
		path = _path;
		releaseMediaPlayer();
		play();
	}*/
	
	public void play() {
		if (PasueFlag) {
			if (mediaPlayer != null) {
				// ��ת�����ż�¼��λ��
				mediaPlayer.seekTo(currentPosition);
				mediaPlayer.start();
			}
			PasueFlag = false;
		} else {
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					System.out.println("��Ƶ���ڲ���״̬");
				}
			} else {
				// ���ݵ�·�� �Ƿ�Ϊ��
				if (path != null) {
					try {

						// ������Ƶ���ŵĶ���
						mediaPlayer = new MediaPlayer();
						/**
						 * ��Ƶ������ɳ������¼�
						 */
						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

							public void onCompletion(MediaPlayer mp) {
								mediaPlayer.release();
								mediaPlayer = null;
								if (pathlist != null && pathlist.size() > 0) {
									_tag++;
									if (_tag >= pathlist.size()) {
										_tag = 0;
										Log.i("onReceive", "���Ͳ�����һ���㲥");
											Bundle bundle = new Bundle();
											bundle.putInt("cmd", 100);
											Intent intent = new Intent();
											intent.putExtras(bundle);
											intent.setAction(Dms.ACTION_MQTT);
											context.sendBroadcast(intent);
									//		return;

									}
									path = pathlist.get(_tag);
									Log.i("TAGTAGTAG", "path------>" + path);
									srtPath = null;
									txtPath = null;
									cancelTextView();

								}
								play();
							}
						});
						mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

							@Override
							public boolean onError(MediaPlayer mp, int what, int extra) {
								Log.i("connect", "MyMediaPlayer OnError");
								mediaPlayer.release();
								mediaPlayer = null;
								return false;
							}
						});
						// �����Ƶ������
						mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
						// ָ�����ŵ��ļ�
						mediaPlayer.setDataSource(path);
						// ָ�����ڲ�����Ƶ��SurfaceView�Ŀؼ�
						mediaPlayer.setDisplay(surfaceHolder);
						
						mediaPlayer.prepare();

						mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
							@Override
							public void onPrepared(MediaPlayer arg0) {
								// TODO Auto-generated method stub
								if (srtPath != null && !"".equals(srtPath)) {
									System.out.println(srtPath);
									playSrt(srtPath);
								}

							}
						});
						mediaPlayer.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
							@Override
							public void onTimedText(MediaPlayer arg0, final TimedText text) {
								// TODO Auto-generated method stub
								if (text != null)
									Dms.zimu.setText(text.getText());
							}
						});
						// ��¼�ϴβ��ŵ�λ��
						mediaPlayer.seekTo(backPosition);
						mediaPlayer.start();
						PasueFlag = false;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void playTxt(String txtPath) {
		File f = new File(txtPath);
		if (f.exists()) {
			String str = FileUtil.convertCodeAndGetText(txtPath);
			System.out.println("Mediaplayer play txt " + txtPath);
			if (str != null && !"".equals(str)) {
				str.replace("/n", "");
				List<String> txtlist = CommonUtil.getTxtList(str);
				rantv = new RandomTextView(Dms.zimu, txtlist, 5);
			}
		}
	}

	public void cancelTextView() {
		if (rantv != null) {
			rantv.release();
			rantv = null;
		}
	}

	public void playSrt(String srtPath) {
		try {
			mediaPlayer.addTimedTextSource(srtPath, MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);
			TrackInfo[] trackInfos = mediaPlayer.getTrackInfo();
			if (trackInfos != null && trackInfos.length > 0) {
				for (int i = 0; i < trackInfos.length; i++) {
					final TrackInfo info = trackInfos[i];

					Log.w(TAG, "TrackInfo: " + info.getTrackType() + " " + info.getLanguage());

					if (info.getTrackType() == TrackInfo.MEDIA_TRACK_TYPE_AUDIO) {
						// mMediaPlayer.selectTrack(i);
					} else if (info.getTrackType() == TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT) {
						mediaPlayer.selectTrack(i);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print(e.toString());
		}

	}

	public void pasueVideo() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			// ��¼���λ��
			currentPosition = mediaPlayer.getCurrentPosition();
			// ��ʶ ����Ƶ �ᱻ��ͣ
			PasueFlag = true;
		}
	}

	// ��ǰ����λ��
	public int getCurrentPosition() {
		return mediaPlayer.getCurrentPosition();

	}

	// �õ���Ƶʱ��
	public int getDuration() {
		return mediaPlayer.getDuration();

	}

	public void pasue() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	public void stopVideo() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			// ��ͣ��ʶ
			PasueFlag = false;
		}

	}

	public void resetVideo() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(0);
			mediaPlayer.start();
		}
	}

	public void releaseMediaPlayer() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
			System.out.println(mediaPlayer + "*****releaseMediaPlayer" + mediaPlayer);
		}
		cancelTextView();
	}
	public void stop() {
		if(mediaPlayer!=null) {
			
		mediaPlayer.stop();
		}
	}
	public void start() {
		if(mediaPlayer!=null) {
		mediaPlayer.start();
		}
	}
}
