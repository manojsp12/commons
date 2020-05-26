package io.mosip.idrepository.identity.test.provider.impl;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;

import io.mosip.idrepository.identity.provider.impl.FingerprintProvider;
import io.mosip.kernel.bioapi.impl.BioApiImpl;
import io.mosip.kernel.core.bioapi.model.Response;
import io.mosip.kernel.core.cbeffutil.entity.BDBInfo;
import io.mosip.kernel.core.cbeffutil.entity.BIR;
import io.mosip.kernel.core.cbeffutil.entity.BIRInfo;
import io.mosip.kernel.core.cbeffutil.jaxbclasses.BIRType;
import io.mosip.kernel.core.cbeffutil.jaxbclasses.ProcessedLevelType;
import io.mosip.kernel.core.cbeffutil.jaxbclasses.PurposeType;
import io.mosip.kernel.core.cbeffutil.jaxbclasses.QualityType;
import io.mosip.kernel.core.cbeffutil.jaxbclasses.RegistryIDType;
import io.mosip.kernel.core.cbeffutil.jaxbclasses.SingleAnySubtypeType;
import io.mosip.kernel.core.cbeffutil.jaxbclasses.SingleType;

/**
 * @author Manoj SP
 *
 */
@RunWith(SpringRunner.class)
public class FingerprintProviderTest {

	@Mock
	private BioApiImpl bioApiImpl;
	
	FingerprintProvider fp = new FingerprintProvider();
	
	@Before
	public void before() {
		ReflectionTestUtils.setField(fp, "bioApiImpl", bioApiImpl);
	}

	@Test
	public void testConvertFIRtoFMR() {
		RegistryIDType registryIDType = new RegistryIDType();
		registryIDType.setOrganization("257");
		registryIDType.setType("7");
		QualityType quality = new QualityType();
		quality.setScore(95l);
		BIR rFinger = new BIR.BIRBuilder().withBdb("3".getBytes())
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(registryIDType)
						.withQuality(quality).withType(Arrays.asList(SingleType.FINGER))
						.withSubtype(Arrays.asList(SingleAnySubtypeType.RIGHT.value(),
								SingleAnySubtypeType.INDEX_FINGER.value()))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now()).build())
				.build();
		BIRType birType = new BIRType();
		birType.setBDB(rFinger.getBdb());
		Response<BIR> response = new Response<>();
		response.setResponse(rFinger);
		Mockito.when(bioApiImpl.extractTemplate(Mockito.any(), Mockito.any())).thenReturn(response);
		List<BIR> data = fp.convertFIRtoFMR(Collections.singletonList(rFinger));
		assertTrue(data.get(0).getBdbInfo().getFormat().getType().contentEquals("2"));
	}

	@Test
	public void testConvertFIRtoFMRUpdate() {
		RegistryIDType registryIDType = new RegistryIDType();
		registryIDType.setOrganization("257");
		registryIDType.setType("7");
		QualityType quality = new QualityType();
		quality.setScore(95l);
		BIR rFinger = new BIR.BIRBuilder().withBdb("3".getBytes())
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(registryIDType)
						.withQuality(quality).withType(Arrays.asList(SingleType.FINGER))
						.withSubtype(Arrays.asList(SingleAnySubtypeType.RIGHT.value(),
								SingleAnySubtypeType.INDEX_FINGER.value()))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now()).build())
				.build();
		BIR rFinger2 = new BIR.BIRBuilder().withBdb("3".getBytes())
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(registryIDType)
						.withQuality(quality).withType(Arrays.asList(SingleType.FINGER))
						.withSubtype(Arrays.asList(SingleAnySubtypeType.RIGHT.value(),
								SingleAnySubtypeType.INDEX_FINGER.value()))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now()).build())
				.build();
		BIRType birType = new BIRType();
		birType.setBDB(rFinger.getBdb());
		Response<BIR> response = new Response<>();
		response.setResponse(rFinger);
		Mockito.when(bioApiImpl.extractTemplate(Mockito.any(), Mockito.any())).thenReturn(response);
		List<BIR> data = fp
				.convertFIRtoFMR(Lists.newArrayList(rFinger, rFinger2));
		assertTrue(data.get(0).getBdbInfo().getFormat().getType().contentEquals("2"));
	}
}
