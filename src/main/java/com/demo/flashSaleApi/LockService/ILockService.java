package com.demo.flashSaleApi.LockService;

public interface ILockService {
	String acquireLockWithTimeout(final String lockName, final Long acquireTimeout, final Long lockTimeout);
	Boolean releaseLock(final String lockName, final String identifier);
}
