package net.minevn.uuidmanager;

import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class UpdateTask implements Runnable {

	private final UuidManager main;
	private final MySQL sql;
	private final ScheduledTask task;

	private boolean isCanceled;

	public UpdateTask(UuidManager main, MySQL sql) {
		this.main = main;
		this.sql = sql;
		this.task = main.getProxy().getScheduler().schedule(main, this, 5, TimeUnit.SECONDS);
	}

	@Override
	public void run() {
		int java = sql.getProxyData("java");
		int bedrock = sql.getProxyData("bedrock");
		UuidManager.setPlayers_count(java + bedrock);
		if (Config.proxy_bedrock) {
			sql.setProxyData("bedrock", main.getProxy().getOnlineCount());
		} else {
			sql.setProxyData("java", main.getProxy().getOnlineCount());
		}
	}

	public void cancel() {
		isCanceled = true;
		task.cancel();
	}

	public boolean isCancel() {
		return isCanceled;
	}
}
