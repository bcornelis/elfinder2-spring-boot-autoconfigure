package com.fidecium.elfinder2.autoconfigure;

import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.bluejoe.elfinder.controller.ConnectorController;
import cn.bluejoe.elfinder.controller.executor.CommandExecutorFactory;
import cn.bluejoe.elfinder.controller.executor.DefaultCommandExecutorFactory;
import cn.bluejoe.elfinder.controller.executors.MissingCommandExecutor;
import cn.bluejoe.elfinder.impl.DefaultFsService;
import cn.bluejoe.elfinder.impl.DefaultFsServiceConfig;
import cn.bluejoe.elfinder.impl.StaticFsServiceFactory;
import cn.bluejoe.elfinder.service.FsSecurityChecker;
import cn.bluejoe.elfinder.service.FsService;
import cn.bluejoe.elfinder.service.FsServiceConfig;
import cn.bluejoe.elfinder.service.FsServiceFactory;
import cn.bluejoe.elfinder.service.FsVolume;

@Configuration
@ConditionalOnClass({ ConnectorController.class })
public class Elfinder2AutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public ConnectorController connectController() {
		return new ConnectorController();
	}

	@Bean
	@ConditionalOnMissingBean
	public CommandExecutorFactory commandExecutorFactory() {
		final DefaultCommandExecutorFactory cef = new DefaultCommandExecutorFactory();
		cef.setClassNamePattern("cn.bluejoe.elfinder.controller.executors.%sCommandExecutor");
		cef.setFallbackCommand(missingCommandExecutor());
		return cef;
	}

	@Bean
	@ConditionalOnMissingBean
	public FsServiceFactory fsServiceFactory(final FsService fsService) {
		final StaticFsServiceFactory sfsf = new StaticFsServiceFactory();
		sfsf.setFsService(fsService);
		return sfsf;
	}

	@Bean
	@ConditionalOnMissingBean
	public FsService fsService(final FsServiceConfig fsServiceConfig, final FsSecurityChecker fsSecurityChecker,
			final Map<String, FsVolume> volumeMap) {
		final DefaultFsService dfs = new DefaultFsService();
		dfs.setSecurityChecker(fsSecurityChecker);
		dfs.setServiceConfig(fsServiceConfig);
		dfs.setVolumeMap(volumeMap);
		return dfs;
	}

	@Bean
	@ConditionalOnMissingBean
	public MissingCommandExecutor missingCommandExecutor() {
		return new MissingCommandExecutor();
	}

	@Bean
	@ConditionalOnMissingBean
	public FsServiceConfig fsServiceConfig() {
		final DefaultFsServiceConfig def = new DefaultFsServiceConfig();
		def.setTmbWidth(80);
		return def;
	}
}
