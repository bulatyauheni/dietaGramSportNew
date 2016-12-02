package bulat.diet.helper_sport;

import java.io.File;
import java.io.IOException;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.os.ParcelFileDescriptor;

public class MyBackupAgent extends BackupAgentHelper{
	   @Override
	public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
			ParcelFileDescriptor newState) throws IOException {
		// TODO Auto-generated method stub
		super.onBackup(oldState, data, newState);
	}

	@Override
	public void onRestore(BackupDataInput data, int appVersionCode,
			ParcelFileDescriptor newState) throws IOException {
		// TODO Auto-generated method stub
		super.onRestore(data, appVersionCode, newState);
	}

	private static final String DB_NAME = "dishessport.db";

	   @Override
	   public void onCreate(){
	      FileBackupHelper dbs = new FileBackupHelper(this, DB_NAME);
	      addHelper("dbs", dbs);
	   }

	   @Override
	   public File getFilesDir(){
	      File path = getDatabasePath(DB_NAME);
	      return path.getParentFile();
	   }
}
