import torch
import torch.nn as nn
from torchvision.models import efficientnet_b0, EfficientNet_B0_Weights

class EfficientNetEmbedding(nn.Module):
    def __init__(self):
        super().__init__()

        weights = EfficientNet_B0_Weights.IMAGENET1K_V1
        model = efficientnet_b0(weights=weights)

        # Remove classifier head
        self.features = model.features
        self.pool = model.avgpool  # global pooling layer

    def forward(self, x):
        x = self.features(x)
        x = self.pool(x)
        x = torch.flatten(x, 1)
        return x
