package com.ctrl.forum.qiniu_main.up.simple;

import com.ctrl.forum.qiniu_main.config.Config;
import com.ctrl.forum.qiniu_main.up.UploadHandler;
import com.ctrl.forum.qiniu_main.up.auth.Authorizer;
import com.ctrl.forum.qiniu_main.up.rs.PutExtra;
import com.ctrl.forum.qiniu_main.up.UpParam.FileUpParam;

import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*import main.com.qiniu.config.Config;
import main.com.qiniu.up.UpParam.FileUpParam;
import main.com.qiniu.up.UploadHandler;
import main.com.qiniu.up.auth.Authorizer;
import main.com.qiniu.up.rs.PutExtra;*/

public class FileSimpleUpload extends SimpleUpload {
	protected final File file;

	public FileSimpleUpload(Authorizer authorizer, String key, FileUpParam upParam, PutExtra extra, Object passParam,
			UploadHandler handler) {
		super(authorizer, key, upParam, extra, passParam, handler);
		this.file = upParam.getFile();
	}

	@Override
	protected AbstractContentBody buildFileBody() {
		if (mimeType != null) {
			return new MyFileBody(file, mimeType);
		} else {
			return new MyFileBody(file);
		}
	}

	protected class MyFileBody extends FileBody {

		public MyFileBody(File file) {
			super(file);
		}

		public MyFileBody(File file, String mimeType) {
			super(file, mimeType);
		}

		public void writeTo(final OutputStream out) throws IOException {
			if (out == null) {
				throw new IllegalArgumentException("Output stream may not be null");
			}
			InputStream in = new FileInputStream(this.getFile());
			try {
				byte[] tmp = new byte[Config.ONCE_WRITE_SIZE];
				int l;
				while ((l = in.read(tmp)) != -1) {
					out.write(tmp, 0, l);
					addSuccessLength(l); // 其它代码和父类一样，只加了这行代码
				}
				out.flush();
			} finally {
				in.close();
			}
		}

	}
}
