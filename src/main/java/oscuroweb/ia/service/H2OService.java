package oscuroweb.ia.service;

import oscuroweb.ia.dto.IncomeDto;
import oscuroweb.ia.dto.OutputDto;
import oscuroweb.ia.dto.OutputResultDto;

public interface H2OService {

	public OutputDto evaluate(IncomeDto input);
	public OutputResultDto addResult(IncomeDto input);
}
