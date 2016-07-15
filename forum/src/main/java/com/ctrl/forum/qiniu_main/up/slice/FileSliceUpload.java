package com.ctrl.forum.qiniu_main.up.slice;

import com.ctrl.forum.qiniu_main.config.Config;
import com.ctrl.forum.qiniu_main.up.UpParam;
import com.ctrl.forum.qiniu_main.up.UploadHandler;
import com.ctrl.forum.qiniu_main.up.auth.Authorizer;
import com.ctrl.forum.qiniu_main.up.rs.PutExtra;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class FileSliceUpload extends SliceUpload {
	protected final RandomAccessFile file;
	private final Lock fileUploadLock;

	private int currentBlockIdx = 0;

	public FileSliceUpload(Authorizer authorizer, String key, UpParam.FileUpParam upParam, PutExtra extra, Object passParam,
			UploadHandler handler) {

		super(authorizer, key, upParam, extra, passParam, handler);
		try {
			this.file = new RandomAccessFile(upParam.getFile(), "r");
			fileUploadLock = new ReentrantLock();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected boolean hasNext() {
		return contentLength > currentBlockIdx * Config.BLOCK_SIZE;
	}

	@Override
	protected UploadBlock buildNextBlockUpload() throws IOException {
		long start = currentBlockIdx * Config.BLOCK_SIZE;
		int len = (int) Math.min(Config.BLOCK_SIZE, contentLength - start);

		FileUploadBlock fb = new FileUploadBlock(this, authorizer, getHttpClient(), Config.UP_HOST, currentBlockIdx, start, len,
				Config.CHUNK_SIZE, Config.FIRST_CHUNK, file, fileUploadLock);

		currentBlockIdx++;

		return fb;
	}

	@Override
	protected void clean() throws Exception {
		super.clean();
		file.close();
	}
}
