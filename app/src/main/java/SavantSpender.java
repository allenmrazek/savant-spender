import android.app.Application;

import com.savantspender.AppExecutors;
import com.savantspender.db.AppDatabase;
import com.savantspender.model.DataRepository;

public class SavantSpender extends Application {
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}
