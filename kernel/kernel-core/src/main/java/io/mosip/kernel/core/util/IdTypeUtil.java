package io.mosip.kernel.core.util;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.mosip.kernel.core.idvalidator.exception.InvalidIDException;
import io.mosip.kernel.core.idvalidator.spi.UinValidator;
import io.mosip.kernel.core.idvalidator.spi.VidValidator;
import io.mosip.kernel.core.util.constant.IdType;

/**
 * @author Manoj SP
 *
 */
@Component
public class IdTypeUtil {

	@Autowired(required = false)
	private UinValidator<String> uinValidator;

	@Autowired(required = false)
	private VidValidator<String> vidValidator;

	public boolean validateUin(String uin) {
		try {
			if (Objects.nonNull(uinValidator))
				return uinValidator.validateId(uin);
			else
				return false;
		} catch (InvalidIDException e) {
			return false;
		}
	}

	public boolean validateVid(String vid) {
		try {
			if (Objects.nonNull(vidValidator))
				return vidValidator.validateId(vid);
			else
				return false;
		} catch (InvalidIDException e) {
			return false;
		}
	}

	public Optional<IdType> getIdType(String id) {
		if (this.validateUin(id))
			return Optional.of(IdType.UIN);
		if (this.validateVid(id))
			return Optional.of(IdType.VID);
		return Optional.ofNullable(null);
	}
}
